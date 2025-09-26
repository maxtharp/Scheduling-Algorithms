import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class RR extends Algorithm{

    private final Queue<Process> readyQueue = new LinkedList<>();

    private final Queue<Process> processesToArrive;

    private final int testCaseTime;
    int now = 0;

    public RR(List<Process> allProcesses, int testCaseTime){
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses);
        this.testCaseTime = testCaseTime;
    }

    @Override
    public void schedule(){
        System.out.println("Round Robin:");

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()) {
            if (readyQueue.isEmpty()) {
                Process process = processesToArrive.remove();
                if (now < process.getArrivalTime()) {
                    //advance the simulation clock to the next process's arrival time
                    now = process.getArrivalTime();
                }
                readyQueue.add(process);
            }

            Process currentProcess = readyQueue.remove();
            int runTime = currentProcess.getBurstTime();
            int remainingTime = currentProcess.getRemainingTime();

            if (runTime < testCaseTime) {
                System.out.print("At time " + now + ": ");
                CPU.run(currentProcess, runTime);
                now += runTime;
                currentProcess.setRemainingTime(0);
                currentProcess.setFinishTime(now);
                while(!processesToArrive.isEmpty() &&
                        processesToArrive.peek().getArrivalTime()<=now){
                    readyQueue.add(processesToArrive.remove());
                }
            }

            else if (remainingTime > testCaseTime) {
                System.out.print("At time " + now + ": ");
                CPU.run(currentProcess, testCaseTime);
                now += testCaseTime ;
                currentProcess.setRemainingTime(remainingTime - testCaseTime);
                currentProcess.setFinishTime(now);
                while(!processesToArrive.isEmpty() &&
                        processesToArrive.peek().getArrivalTime()<=now){
                    readyQueue.add(processesToArrive.remove());
                }
                readyQueue.add(currentProcess);
            }
        }
    }
}
