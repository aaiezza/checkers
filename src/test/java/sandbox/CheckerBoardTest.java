package sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CheckerBoardTest {
  @Test
  void shouldPrintBoardAsExpected() {
    final CheckerBoard subject = CheckerBoard.freshBoard();

    assertThat(subject.toString())
    .
      .isEqualTo("""
            Ω · Ω · Ω · Ω ·
            · Ω · Ω · Ω · Ω
            Ω · Ω · Ω · Ω ·
            · • · • · • · •
            • · • · • · • ·
            · ☺ · ☺ · ☺ · ☺
            ☺ · ☺ · ☺ · ☺ ·
            · ☺ · ☺ · ☺ · ☺
            """);
  }
}
