package edu.hcu.triage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
 * Stores all patients by ID for O(1)-ish lookup.
 * Handles registration and simple updates.
 */
public class PatientRegistry {

    private final Map<String, Patient> byId = new HashMap<>();
    private long nextArrivalSeq = 0L;

    /* Registers a brand-new patient. */
    public Patient registerNew(String id, String name, int age, int severity) {
        validate(id, name, age, severity);

        Patient p = new Patient(id, name, age, severity, nextArrivalSeq++);
        byId.put(id, p);
        return p;
    }

    /*
     * Updates an existing patient.
     * Any field may be null or -1 to indicate "no change".
     */
    public boolean updateExisting(String id, String name, Integer age, Integer severity) {
        Patient p = byId.get(id);
        if (p == null) return false;

        if (name != null && !name.isBlank()) {
            p.setName(name);
        }
        if (age != null && age > 0) {
            p.setAge(age);
        }
        if (severity != null && severity >= 1 && severity <= 10) {
            p.setSeverity(severity);
        }

        return true;
    }

    /* Optional-returning lookup. */
    public Optional<Patient> get(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public boolean contains(String id) {
        return byId.containsKey(id);
    }

    public int size() {
        return byId.size();
    }

    /** Basic input validation. */
    private void validate(String id, String name, int age, int severity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive.");
        }
        if (severity < 1 || severity > 10) {
            throw new IllegalArgumentException("Severity must be between 1 and 10.");
        }
    }
}
