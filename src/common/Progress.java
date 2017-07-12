package common;

import helpers.Statistics;

public class Progress {
    private StringBuilder progress;
    private int thread;
    private Statistics statistics;

    public Progress(int thr, long filesize, String filename) {
        this.statistics = new Statistics();
        this.statistics.setTransferSize(filesize);
        this.statistics.setFilename(filename);
        this.statistics.setStartTime(System.nanoTime());
        this.thread = thr;
        init();
    }

    /**
     * Class initialization
     */
    private synchronized void init() {
        this.progress = new StringBuilder(60);
    }

    /**
     * Updates progress bar in command line output
     * @param done
     */
    public synchronized void update(long done) {
        String formatFilename = "\r %s %s %s %s seconds";

        long percent = (done++ * 100) / this.statistics.getTransferSize();
        String offset = "";
        int filenameLen = this.statistics.getFilename().length();
        for (int i = 0; i < (filenameLen + 15)*(this.thread); i++){
            offset = offset + " ";
        }

        long seconds = (System.nanoTime() - this.statistics.getStartTime())/1000000000;

        String outputProgress = String.format(formatFilename, offset, this.statistics.getFilename(), percent + "%", seconds);

        System.out.print(outputProgress);

        if (done == this.statistics.getTransferSize()) {
            System.out.flush();
            System.out.println();
            init();
        }
    }

    public synchronized Statistics complete(){
        this.statistics.setCompletionTime(System.nanoTime());
        return this.statistics;
    }
}