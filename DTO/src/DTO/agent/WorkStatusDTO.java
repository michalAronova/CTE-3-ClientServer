package DTO.agent;

public class WorkStatusDTO {
    private int candidatesProduced;
    private int missionDone;
    private int missionLeftInQueue;

    public WorkStatusDTO(){
        this.candidatesProduced = 0;
        this.missionDone = 0;
        this.missionLeftInQueue = 0;
    }
    public WorkStatusDTO(int candidatesProduced, int missionDone, int missionsLeft) {
        this.candidatesProduced = candidatesProduced;
        this.missionDone = missionDone;
        this.missionLeftInQueue = missionsLeft;
    }

    public void reset(){
        this.candidatesProduced = 0;
        this.missionDone = 0;
        this.missionLeftInQueue = 0;
    }

    public int getMissionLeftInQueue() {
        return missionLeftInQueue;
    }

    public int getCandidatesProduced() {
        return candidatesProduced;
    }

    public int getMissionDone() {
        return missionDone;
    }

    public int getTotalMissions() {
        return missionDone + missionLeftInQueue;
    }
}
