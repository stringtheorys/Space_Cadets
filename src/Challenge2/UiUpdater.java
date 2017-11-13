package Challenge2;

import java.util.concurrent.ConcurrentLinkedQueue;

class UiUpdater extends Thread {

  private int uiWaitTime = 100;
  private int interpreterWaitTime = 100;

  private MenuScreen menuScreen;
  private Block[][] outputGrid;
  private int gridSize;
  private ConcurrentLinkedQueue<boolean[][]> outputGridQueue;

  UiUpdater(MenuScreen newMenuScreen, Block[][] newOutputGrid, int newGridSize,
      ConcurrentLinkedQueue<boolean[][]>
          newOutputGridQueue) {
    menuScreen = newMenuScreen;
    outputGrid = newOutputGrid;
    gridSize = newGridSize;
    outputGridQueue = newOutputGridQueue;
  }

  @Override
  public void run() {

    Log.print("UiUpdater starts");

    while (menuScreen.getUiUpdateStatus() == MenuScreen.UiUpdateStatus.RUNNING) {

      if (outputGridQueue.isEmpty() == false) {
        Log.defPrint("UI : Run later on ui update size :" + outputGridQueue.size());

        Log.defPrint("Updating");
        boolean[][] updateGrid = outputGridQueue.poll();
        for (int x = 0; x < gridSize; x++) {
          for (int y = 0; y < gridSize; y++) {
            outputGrid[x][y].setColour(updateGrid[x][y]);

          }
        }

        try {
          sleep(uiWaitTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      } else if (menuScreen.getInterpreterStatus() == MenuScreen.InterpreterStatus.RUNNING) {
        Log.defPrint("UI : Interpreter running but no ui outputs");
        try {
          Thread.sleep(interpreterWaitTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        Log.defPrint("UI : Interpreter ended and OutputGridQueue is empty.");

        menuScreen.uiUpdaterStopped();
        break;
      }
    }
    Log.print("UI : UiUpdater Stopped");

  }

  // Updates the UI
  void updateUi(boolean[][] grid) {
    Log.defPrint("MS : Updating Ui");

    for (int x = 0; x < gridSize; x++) {
      for (int y = 0; y < gridSize; y++) {
        outputGrid[x][y].setColour(grid[x][y]);
      }
    }
  }
}
