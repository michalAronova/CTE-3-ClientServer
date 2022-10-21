package engine.decipherManager.resultListener;

import DTO.missionResult.MissionResult;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class ResultListener implements Runnable{
    private final BlockingQueue<MissionResult> resultQueue;
    private final Consumer<MissionResult> transferMissionResult;
    private BooleanProperty proceed = null;

    public ResultListener(BlockingQueue<MissionResult> resultQueue, Consumer<MissionResult> transferMissionResult) {
        this.resultQueue = resultQueue;
        this.transferMissionResult = transferMissionResult;
    }

    public ResultListener addBooleanToProceed(BooleanProperty proceed){
        this.proceed = proceed;
        return this;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                if(proceed != null && !proceed.getValue()){
                    break;
                }
                transferMissionResult.accept(resultQueue.take());
            }
        }
        catch(InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
