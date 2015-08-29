
// Calling Activity
set = new TransitionSet();
fade = new Fade();
slide = new Slide(Gravity.BOTTOM);
propagation = new CircularPropagation();
propagation.setPropagationSpeed(1f);
set.addTransition(fade).addTransition(slide);
set.setPropagation(propagation);
set.setOrdering(ORDERING_TOGETHER);
getWindow().setExitTransition(set);
