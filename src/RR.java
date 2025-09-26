import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RR extends Algorithm {

    private final Queue<Process> readyQueue = new LinkedList<>();
    private final Queue<Process> processesToArrive;
    private final int testCaseTime;
    int now = 0;

    public RR(List<Process> allProcesses, int testCaseTime) {
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime))
                .toList());
        this.testCaseTime = testCaseTime;
    }

    @Override
    public void schedule() {
        System.out.println("Round Robin:");

        for (Process p : allProcesses) {
            p.setRemainingTime(p.getBurstTime());
            p.setFinishTime(-1);
        }

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
            int remainingTime = currentProcess.getRemainingTime();

            //run for either remainingTime or quantum, whichever is smaller
            int run = Math.min(remainingTime, testCaseTime);

            System.out.print("At time " + now + ": ");
            CPU.run(currentProcess, run);

            now += run;
            currentProcess.setRemainingTime(remainingTime - run);

            while (!processesToArrive.isEmpty() &&
                    processesToArrive.peek().getArrivalTime() <= now) {
                readyQueue.add(processesToArrive.remove());
            }

            if (currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            } else {
                currentProcess.setFinishTime(now);
            }
        }
    }
}
