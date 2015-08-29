private ObjectAnimator
        createMatrixAnimator(TextureVideoView videoView,
                             Matrix startMatrix,
                             Matrix endMatrix) {
  return ObjectAnimator.ofObject(videoView,
                ANIMATED_TRANSFORM_PROPERTY,
                new MatrixEvaluator(),
                startMatrix, endMatrix);
}
