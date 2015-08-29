public static class GridTransition extends TransitionSet {
  public GridTransition() {
    TransitionSet bounds = new TransitionSet();
    bounds.addTransition(new ChangeBounds())
          .addTransition(new ChangeImageTransform())
          .addTransition(new ChangeVideoTransform());
    setOrdering(ORDERING_SEQUENTIAL);
    addTransition(new Fade(Fade.OUT)).
       addTransition(bounds).
       addTransition(new Fade(Fade.IN));
    }
}
