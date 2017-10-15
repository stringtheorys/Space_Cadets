package Challenge2;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UiUpdater extends Thread {

    private int uiUpdateTime = 250;
    private int interpreterUpdateTime = 100;

    private ConcurrentLinkedQueue<boolean[][]> outputGridQueue;
    private MenuScreen menuScreen;

    UiUpdater(MenuScreen newMenuScreen) {
        menuScreen = newMenuScreen;
    }

    @Override
    public void run() {
        super.run();

        Log.print("UiUpdater starts");

        while (menuScreen.getUiUpdateStatus() == MenuScreen.UiUpdateStatus.RUNNING) {

            if (outputGridQueue.isEmpty() == false) {
                Log.print("UI : Run later on ui update size :" + outputGridQueue.size());
                menuScreen.updateUi(outputGridQueue.poll());

                try {
                    sleep(uiUpdateTime);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (menuScreen.getInterpreterStatus() == MenuScreen.InterpreterStatus.RUNNING) {
                Log.print("UI : Interpreter running but no ui outputs");
                try {
                    sleep(interpreterUpdateTime);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.print("UI : Interpreter ended and OutputGridQueue is empty.");

                menuScreen.uiUpdaterStopped();
                break;
            }
        }
        Log.print("UI : UiUpdater Stopped");

    }
}
