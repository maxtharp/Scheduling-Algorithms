
import java.util.*;

/**
 * TODO: Implement the non-preemptive SJF (Shortest-Job First) scheduling algorithm.
 */

public class SJF extends Algorithm {

    public SJF(List<Process> allProcessList){
        super(allProcessList);
    }

    @Override
    public void schedule() {
        // Create a copy of the processes to work with
        List<Process> processes = new ArrayList<>(allProcesses);
        // Priority queue to store available processes, ordered by burst time (shortest first)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getBurstTime)
        );

        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();

        while (completedProcesses < totalProcesses) {
            // Add all processes that have arrived by current time to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getFinishTime() == -1) {
                    readyQueue.add(p);
                }
            }

            // If no processes are ready, advance time to the next arrival
            if (readyQueue.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : processes) {
                    if (p.getArrivalTime() > currentTime && p.getFinishTime() == -1) {
                        nextArrival = Math.min(nextArrival, p.getArrivalTime());
                    }
                }
                if (nextArrival != Integer.MAX_VALUE) {
                    currentTime = nextArrival;
                    continue;
                }
                break;
            }

            // Get the process with the shortest burst time
            Process currentProcess = readyQueue.poll();

            // Run the process to completion (non-preemptive)
            int burstTime = currentProcess.getBurstTime();
            CPU.run(currentProcess, burstTime);

            // Update finish time
            currentProcess.setFinishTime(currentTime + burstTime);
            currentTime += burstTime;
            completedProcesses++;

            // Clear the ready queue as we need to re-check for newly arrived processes
            readyQueue.clear();
        }
    }
}
