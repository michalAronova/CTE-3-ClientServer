package engine.decipherManager.missionTaker;

import DTO.missionResult.MissionResult;
import engine.decipherManager.mission.Mission;

import java.util.concurrent.BlockingQueue;

public class MissionTaker implements Runnable{
    private BlockingQueue<Runnable> alliesWorkQueue;
    private BlockingQueue<Runnable> agentWorkQueue;
    BlockingQueue<MissionResult> agentResultQueue;
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
        if(agentWorkQueue.isEmpty()){
            for (int i = 0; i < missionAmountPull; i++) {
                try {
                    Mission mission = (Mission)alliesWorkQueue.take();
                    mission.setResultQueue(agentResultQueue);
                    agentWorkQueue.put(mission);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
