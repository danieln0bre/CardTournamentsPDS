package br.ufrn.imd.model;

public class Pairing {
    private final String entityOneId;
    private final String entityTwoId;
    private int result;

    public static final int NO_RESULT = -1;

    public Pairing(String entityOneId, String entityTwoId) {
        this.entityOneId = entityOneId;
        this.entityTwoId = entityTwoId;
        this.result = NO_RESULT;
    }

    public String getEntityOneId() {
        return entityOneId;
    }

    public String getEntityTwoId() {
        return entityTwoId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        if (result < 0) {
            throw new IllegalArgumentException("Result cannot be negative.");
        }
        this.result = result;
    }
}
