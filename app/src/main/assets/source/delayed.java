  // Showing register fields...
  TransitionManager.beginDelayedTransition(regContainer);

  editUsername.setVisibility(View.VISIBLE);
  editPass.setVisibility(View.VISIBLE);
  editPass2.setVisibility(View.VISIBLE);
  buttonReg.setVisibility(View.VISIBLE);

  // Hiding it...
  TransitionManager.beginDelayedTransition(regContainer);
  editUsername.setVisibility(View.GONE);
  editPass.setVisibility(View.GONE);
  ...
