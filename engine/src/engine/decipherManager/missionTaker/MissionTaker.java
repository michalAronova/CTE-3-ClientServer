package engine.decipherManager.missionTaker;

import DTO.missionResult.MissionResult;
import engine.decipherManager.mission.Mission;

import java.util.concurrent.BlockingQueue;

public class MissionTaker implements Runnable{
    private final BlockingQueue<Runnable> alliesWorkQueue;
    private final BlockingQueue<Runnable> agentWorkQueue;
    private final BlockingQueue<MissionResult> agentResultQueue;
    private final int missionAmountPull;

    public MissionTaker(BlockingQueue<Runnable> alliesWorkQueue, BlockingQueue<Runnable> agentWorkQueue,
                        BlockingQueue<MissionResult> agentResultQueue, int missionAmountPull) {
        this.alliesWorkQueue = alliesWorkQueue;
        this.agentWorkQueue = agentWorkQueue;
        this.agentResultQueue = agentResultQueue;
        this.missionAmountPull = missionAmountPull;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (agentWorkQueue.isEmpty()) {
                    for (int i = 0; i < missionAmountPull; i++) {
                        Mission mission = (Mission) alliesWorkQueue.take();
                        //mission.setResultQueue(agentResultQueue); //TODO: check if line needed
                        agentWorkQueue.put(mission);
                    }
                }
            }
        }
        catch(InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
