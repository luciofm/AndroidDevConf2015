  TransitionSet set = new TransitionSet();
  set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
  Pop pop = new Pop();
  pop.setPropagation(new CircularPropagation());
  pop.setEpicenterCallback(new Transition.EpicenterCallback() {
    @Override
    public Rect onGetEpicenter(Transition transition) {
      container3.getLocationOnScreen(loc);
      return new Rect(loc[0], loc[1], loc[0] + container3.getWidth(), loc[1] + 40);
    }
  });

  set.addTransition(new ChangeBounds()).addTransition(pop);
  TransitionManager.beginDelayedTransition(container3, set);
