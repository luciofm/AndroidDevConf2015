  // onAppear...
  view.setScaleX(0f);
  view.setScaleY(0f);

  PropertyValuesHolder[] pvh = new PropertyValuesHolder[2];
  pvh[0] = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
  pvh[1] = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);

  ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
  anim.setInterpolator(new OvershootInterpolator());

  return anim;
