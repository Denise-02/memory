/*package com.game.memory;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

public class TimeBar implements Runnable{
    ProgressIndicator progressIndicator;

    public TimeBar(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }












    @Override
    public void run() {
     //   System.out.println("progress = " + progressBar.getProgress());
        while(progressIndicator.getProgress() <= 1) {
            System.out.println("progress = " + progressIndicator.getProgress());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progressIndicator.setProgress(progressIndicator.getProgress() + 0.1);
                }
            });
            synchronized (this) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
        System.out.println("finito");

    }
}
*/