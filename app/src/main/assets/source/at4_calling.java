
// Calling Activity
slide = new Slide(Gravity.LEFT);
propagation = new CircularPropagation();
propagation.setPropagationSpeed(1f);
slide.setPropagation(propagation);
getWindow().setExitTransition(slide);
getWindow().setReenterTransition(slide);
