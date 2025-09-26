import java.util.*;

public class SRTF extends Algorithm {

    public SRTF(List<Process> allProcesses) {
        super(allProcesses);
    }

    @Override
    public void schedule() {
        System.out.println("Shortest Remaining Time First (Preemptive):");

        //sorted by arrival time
        Queue<Process> processesToArrive = new LinkedList<>(
                allProcesses.stream()
                        .sorted(Comparator.comparingInt(Process::getArrivalTime))
                        .toList()
        );

        //sorted by shortest remaining time first
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
        );

        int now = 0;

        for (Process p : allProcesses) {
            p.setRemainingTime(p.getBurstTime());
            p.setFinishTime(-1);
        }

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()) {
            if (readyQueue.isEmpty()) {
                Process next = processesToArrive.remove();
                now = Math.max(now, next.getArrivalTime());
                readyQueue.add(next);
            }

            //get process with the least remaining time
            Process current = readyQueue.poll();

            final int runFor = getRunFor(processesToArrive, now, current);

            System.out.print("At time " + now + ": ");
            CPU.run(current, runFor);
            now += runFor;

            assert current != null;
            current.setRemainingTime(current.getRemainingTime() - runFor);

            while (!processesToArrive.isEmpty() &&
                    processesToArrive.peek().getArrivalTime() <= now) {
                readyQueue.add(processesToArrive.remove());
            }

            if (current.getRemainingTime() > 0) {
                readyQueue.add(current);
            } else {
                current.setFinishTime(now);
            }
        }
    }

    private static int getRunFor(Queue<Process> processesToArrive, int now, Process current) {
        int runFor;
        if (!processesToArrive.isEmpty()) {
            int nextArrival = processesToArrive.peek().getArrivalTime();
            int timeUntilNextArrival = nextArrival - now;

            assert current != null;
            runFor = Math.min(current.getRemainingTime(), timeUntilNextArrival);
        } else {
            assert current != null;
            runFor = current.getRemainingTime();
        }
        return runFor;
    }
}