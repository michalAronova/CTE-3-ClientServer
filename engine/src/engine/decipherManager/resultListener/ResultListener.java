package engine.decipherManager.resultListener;

import DTO.missionResult.MissionResult;
import javafx.concurrent.Task;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class ResultListener implements Runnable{
    private final BlockingQueue<MissionResult> resultQueue;
    private final Consumer<MissionResult> transferMissionResult;

    public ResultListener(BlockingQueue<MissionResult> resultQueue, Consumer<MissionResult> transferMissionResult) {
        this.resultQueue = resultQueue;
        this.transferMissionResult = transferMissionResult;
    }

    @Override
    public void run() {
        try{
            System.out.println("attempting to take...");
            while (!Thread.currentThread().isInterrupted()){
                transferMissionResult.accept(resultQueue.take());
                //System.out.println(resultQueue.take());
            }
        }
        catch(InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
