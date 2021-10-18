package com.example.numbergame.game.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MoveType {
  FIRST_MOVE(Collections.emptyList()),
  REGULAR_MOVE(Arrays.asList(-1, 0, 1));

  private List<Integer> inputOptions;

  MoveType(List<Integer> inputOptions) {
    this.inputOptions = inputOptions;
  }

  public List<Integer> getInputOptions() {
    return inputOptions;
  }
}
