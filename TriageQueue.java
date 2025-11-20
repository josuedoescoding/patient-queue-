package edu.hcu.triage;

import java.util.*;

/*
 * TriageQueue wraps a PriorityQueue and makes sure patients
 * are ordered correctly:
 *  - Higher severity goes first
 *  - If severity is the same, the one who arrived earlier goes first
 */
public class TriageQueue {

    // PriorityQueue that uses our TriageOrder comparator
    private final PriorityQueue<Patient> pq =
            new PriorityQueue<>(new TriageOrder());

    /*
     * Adds a patient directly into the triage queue.
     * This does NOT check for duplicates — the HospitalApp
     * should control when patients are added.
     */
    public void enqueue(Patient p) {
        if (p != null) {
            pq.add(p);
        }
    }

    /*
     * Looks up a patient in the registry using their ID
     * and enqueues them if found.
     * Returns true if added, false if no such ID exists.
     */
    public boolean enqueueById(PatientRegistry reg, String id) {
        Optional<Patient> found = reg.get(id);
        if (found.isEmpty()) {
            return false; // ID not in registry
        }
        pq.add(found.get());
        return true;
    }

    /**
     * Returns the next patient WITHOUT removing them.
     * If queue is empty, Optional will be empty.
     */
    public Optional<Patient> peekNext() {
        return Optional.ofNullable(pq.peek());
    }

    /*
     * Removes and returns the next patient in line.
     * If queue is empty, Optional will be empty.
     */
    public Optional<Patient> dequeueNext() {
        return Optional.ofNullable(pq.poll());
    }

    /* Returns how many patients are currently waiting. */
    public int size() {
        return pq.size();
    }

    /*
     * Returns a snapshot of the queue in sorted order,
     * but does NOT modify the real queue.
     *
     * We copy the PQ and poll from the copy so we get
     * the actual triage order.
     */
    public List<Patient> snapshotOrder() {
        PriorityQueue<Patient> copy = new PriorityQueue<>(pq);
        List<Patient> result = new ArrayList<>();

        while (!copy.isEmpty()) {
            result.add(copy.poll());
        }

        return result;
    }

    /* Removes all patients from the queue. */
    public void clear() {
        pq.clear();
    }
}

