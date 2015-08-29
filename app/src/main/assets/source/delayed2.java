  TransitionManager.beginDelayedTransition(regContainer,
                                  new AutoTransition());

  // AutoTransition
  TransitionSet set = new TransitionSet();
  set.setOrdering(ORDERING_SEQUENTIAL);
  set.addTransition(new Fade(Fade.OUT))
     .addTransition(new ChangeBounds())
     .addTransition(new Fade(Fade.IN));
