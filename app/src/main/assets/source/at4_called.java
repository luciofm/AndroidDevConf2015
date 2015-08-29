
// Called Activity
slide = new Slide(Gravity.RIGHT);
propagation = new CircularPropagation();
propagation.setPropagationSpeed(1f);
getWindow().setEnterTransition(slide);
getWindow().setReturnTransition(slide);
