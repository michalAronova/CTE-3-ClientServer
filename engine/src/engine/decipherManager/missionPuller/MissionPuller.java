package engine.decipherManager.missionPuller;

import DTO.missionResult.MissionResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MissionPuller implements Runnable{

    private final BlockingQueue<Runnable> agentWorkQueue;
    private BlockingQueue<MissionResult> agentResultQueue;
    private final int missionAmountPull;
    private BooleanProperty isEmptyWorkQueue;

    public MissionPuller(BlockingQueue<Runnable> agentWorkQueue,
                         BlockingQueue<MissionResult> agentResultQueue,
                         int missionAmountPull, BooleanProperty isEmptyWorkQueue) {
        this.agentWorkQueue = agentWorkQueue;
        this.agentResultQueue = agentResultQueue;
        this.missionAmountPull = missionAmountPull;
        this.isEmptyWorkQueue = isEmptyWorkQueue;
    }

    @Override
    public void run() {
        List<Runnable> missions = new LinkedList<>();
        System.out.println("Using thread: "+ Thread.currentThread().getName());
//        try {
//            System.out.println(Thread.currentThread().getName()+" going to sleep");
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            System.out.println(Thread.currentThread().getName()+"was interrupted during sleep");
//        }

        while (!Thread.currentThread().isInterrupted()){


            System.out.println("pulled "+ missions.size()+" missions!");
            missions.clear();
        }
    }
}

