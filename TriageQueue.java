package edu.hcu.triage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Thin wrapper around PriorityQueue to enforce the triage policy and provide extra utilities.
 */
public class TriageQueue {
    private final PriorityQueue<Patient> pq = new PriorityQueue<>(new TriageOrder());

    // Enqueue patient object
    public synchronized void enqueue(Patient p) {
        if (p == null) throw new IllegalArgumentException("patient required");
        pq.offer(p);
    }

    // Enqueue by id using registry lookup
    public synchronized boolean enqueueById(PatientRegistry reg, String id) {
        if (reg == null || id == null) return false;
        Optional<Patient> op = reg.get(id);
        if (op.isEmpty()) return false;
        pq.offer(op.get());
        return true;
    }

    public synchronized Optional<Patient> peekNext() {
        return Optional.ofNullable(pq.peek());
    }

    public synchronized Optional<Patient> dequeueNext() {
        return Optional.ofNullable(pq.poll());
    }

    public synchronized int size() {
        return pq.size();
    }

    // Snapshot the queue in sorted order without mutating the original queue
    public synchronized List<Patient> snapshotOrder() {
        PriorityQueue<Patient> copy = new PriorityQueue<>(pq);
        List<Patient> list = new ArrayList<>();
        while (!copy.isEmpty()) list.add(copy.poll());
        return Collections.unmodifiableList(list);
    }

    public synchronized void clear() {
        pq.clear();
    }
}
