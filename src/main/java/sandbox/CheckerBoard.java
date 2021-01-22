package sandbox;

import static one.util.streamex.IntStreamEx.range;
import static sandbox.CheckerBoard.Contents.BLACK;
import static sandbox.CheckerBoard.Contents.RED;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import one.util.streamex.StreamEx;
import sandbox.CheckerBoard.Space.BlackSpace;
import sandbox.CheckerBoard.Space.RedSpace;

@SuppressWarnings("preview")
public class CheckerBoard {
  private static int MAX = 8;
  private final Space[][] spaces;

  private CheckerBoard() {
    spaces = new Space[MAX][MAX];
    forEachSpace(
        (i, j) -> spaces[i][j] = ((i + j) % 2 == 0) ? new BlackSpace(i, j) : new RedSpace(i, j));
  }
  
  private CheckerBoard(final CheckerBoard board) {
    this.spaces = new Space[board.spaces.length][board.spaces[0].length];
    final AtomicInteger row = new AtomicInteger();
    final AtomicInteger col = new AtomicInteger();
    board.forEachSpace(space -> {
      if(space.isRightEdge()) row.incrementAndGet();
      spaces[row.get()][col.getAndIncrement()] = space;
    });
  }

  public static CheckerBoard freshBoard() {
    final CheckerBoard board = new CheckerBoard();

    board.forTopThreeRowsOfSpaces(space -> {
      if (space instanceof BlackSpace s) {
        board.spaces[s.x()][s.y()] = s.withContents(BLACK);
      }
    });

    board.forBottomThreeRowsOfSpaces(space -> {
      if (space instanceof BlackSpace s) {
        board.spaces[s.x()][s.y()] = s.withContents(RED);
      }
    });

    return board;
  }

  public StreamEx<Space> streamSpaces() {
    return range(MAX).flatMapToObj(i -> range(MAX).mapToObj(j -> spaces[i][j]));
  }

  private static void forEachSpace(final BiConsumer<Integer, Integer> modBoard) {
    range(MAX).forEach(i -> range(MAX).forEach(j -> modBoard.accept(i, j)));
  }

  private void forEachSpace(final Consumer<Space> peekSpace) {
    forEachSpace((i, j) -> peekSpace.accept(spaces[i][j]));
  }

  private void forTopThreeRowsOfSpaces(final Consumer<Space> peekSpace) {
    forEachSpace(space -> {
      if (space.x() < 3) {
        peekSpace.accept(space);
      }
    });
  }

  private void forBottomThreeRowsOfSpaces(final Consumer<Space> peekSpace) {
    forEachSpace(space -> {
      if (space.x() >= (MAX - 3)) {
        peekSpace.accept(space);
      }
    });
  }

  @Override
  public String toString() {
    return streamSpaces()
      .map(space -> {
        final String strSpace = (switch (space.contents()) {
          case EMPTY:
            yield space instanceof RedSpace? "·" : "•";
          case BLACK:
            yield "Ω";
          case RED:
            yield "☺";
        });

        return String.format(
            "%s%s",
            strSpace,
            space.isRightEdge()
              ? "\n"
              : " ");
      })
      .reduce(new StringBuilder(), StringBuilder::append, (l, r) -> l)
      .toString();
  }

  public enum Contents {
    EMPTY, BLACK, RED;
  }

  public static sealed interface Space permits BlackSpace,RedSpace {
    public Contents contents();

    public int x();

    public int y();

    public Space withContents(final Contents state);

    public default boolean isEmpty() {
      return contents() == Contents.EMPTY;
    }

    public default boolean contains(final Contents state) {
      return contents() == state;
    }

    public default boolean isLeftEdge() {
      return y() == 0;
    }

    public default boolean isRightEdge() {
      return y() == MAX - 1;
    }

    public default boolean isTopEdge() {
      return x() == 0;
    }

    public default boolean isBottomEdge() {
      return x() == MAX - 1;
    }

    public default boolean isUpperLeftCorner() {
      return isLeftEdge() && isTopEdge();
    }

    public default boolean isLowerRightCorner() {
      return isRightEdge() && isBottomEdge();
    }

    public record BlackSpace(Contents contents, int x, int y) implements Space {
      public BlackSpace(final int x, final int y) {
        this(Contents.EMPTY, x, y);
      }

      @Override
      public Space withContents(final Contents contents) {
        return new BlackSpace(contents, x, y);
      }
    }

    public record RedSpace(Contents contents, int x, int y) implements Space {
      public RedSpace(final int x, final int y) {
        this(Contents.EMPTY, x, y);
      }

      @Override
      public Space withContents(final Contents contents) {
        return new RedSpace(contents, x, y);
      }
    }
  }
}
