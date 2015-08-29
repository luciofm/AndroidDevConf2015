@Override
public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
  TransitionValues endValues) {
  if (startBounds == null || endBounds == null)
    return null;

  boolean matricesEqual = startMatrix.equals(endMatrix);

  if (startBounds.equals(endBounds) && matricesEqual)
    return null;

  ObjectAnimator animator;
  animator = createMatrixAnimator(videoView, startMatrix, endMatrix);
  return animator;
}
