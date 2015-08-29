private void captureValues(TransitionValues transitionValues) {
  View view = transitionValues.view;
  if (!(view instanceof TextureVideoView)) {
    return;
  }
  TextureVideoView videoView = (TextureVideoView) view;
  Map<String, Object> values = transitionValues.values;

  int left = view.getLeft(); ...

  Rect bounds = new Rect(left, top, right, bottom);
  values.put(PROPNAME_BOUNDS, bounds);
  Matrix matrix;
  matrix = new Matrix(videoView.getTransformMatrix());
  values.put(PROPNAME_MATRIX, matrix);
}
