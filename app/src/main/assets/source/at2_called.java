
// Called Activity
slide = new Slide(Gravity.RIGHT);
fade = new Fade();
slide.addTarget(R.id.imagesHeader);
set = new TransitionSet();
set.addTransition(slide).addTransition(fade);
getWindow().setEnterTransition(set);
