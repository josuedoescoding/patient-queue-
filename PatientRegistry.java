package edu.hcu.triage;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * O(1)-ish lookup of patients by id. Also assigns arrival sequence numbers.
 */
public class PatientRegistry {
    private final Map<String, Patient> byId = new HashMap<>();
    private long nextArrivalSeq = 0L;

    // Register a new patient (or overwrite if id exists)
    public synchronized Patient registerNew(String id, String name, int age, int severity) {
        Instant now = Instant.now();
        long seq = nextArrivalSeq++;
        Patient p = new Patient(id, name, age, severity, now, seq);
        byId.put(id, p);
        return p;
    }

    // Partial update: update only non-null/positive fields passed
    public synchronized Optional<Patient> updateExisting(String id, String name, Integer age, Integer severity) {
        Patient p = byId.get(id);
        if (p == null) return Optional.empty();
        if (name != null) p.setName(name);
        if (age != null) p.setAge(age);
        if (severity != null) p.setSeverity(severity);
        return Optional.of(p);
    }

    public synchronized Optional<Patient> get(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public synchronized boolean contains(String id) {
        return byId.containsKey(id);
    }

    public synchronized int size() {
        return byId.size();
    }
}
