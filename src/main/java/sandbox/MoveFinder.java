package sandbox;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import one.util.streamex.StreamEx;
import sandbox.CheckerBoard.Contents;
import sandbox.CheckerBoard.Space;
import sandbox.MoveFinder.Move.Description;

@SuppressWarnings("preview")
public record MoveFinder() {

  public List<Move> findMoves(final CheckerBoard board, final CheckerBoard.Contents player) {
    if (null == player || player == Contents.EMPTY) {
      throw new IllegalArgumentException("BLACK or RED player must be passed in.");
    }

    return board
        .streamSpaces()
        .filter(spaceContainsTokenOf(player))
        .flatMap(allTokenMoves(board, player))
        .chain(chooseOnlyMovesThatTakeOtherPlayerToken(player));
  }

  private static Predicate<Space> spaceContainsTokenOf(final CheckerBoard.Contents player) {
    return space -> space.contains(player);
  }

  private static Function<Space, StreamEx<Move>> allTokenMoves(
      final CheckerBoard board, final CheckerBoard.Contents player) {
    // TODO
    return space -> {
      final List<Space> diagonals = null;
      // TODO
      // space
      //          .getAllDiagonals() : space.getForwardDiagonals();

      return StreamEx.of(diagonals)
          .mapPartial(
              diagSpace -> {
                if (diagSpace.isEmpty()) {
                  return Optional.of(
                      new Move(
                          // TODO apply move and make new board
                          board, new Description("Regular move in some direction?")));
                } else if (diagSpace.contains(player)) {
                  return Optional.empty();
                } else {
                  // TODO: figure out jumping
                  return Optional.empty();
                }
              });
    };
  }

  private Function<StreamEx<Move>, List<Move>> chooseOnlyMovesThatTakeOtherPlayerToken(
      final CheckerBoard.Contents player) {
    // TODO
    return moves -> moves.toList();
  }

  public record Move(CheckerBoard board, Description description) {
    public record Description(String value) {}
  }
}
