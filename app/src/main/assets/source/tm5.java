Property<TextureVideoView, Matrix> ANIMATED_TRANSFORM_PROPERTY
   = new Property<TextureVideoView, Matrix>(Matrix.class, "animatedTransform") {
  
  @Override
  public void set(TextureVideoView object, Matrix value) {
    object.setTransform(value);
    object.invalidate();
   }

  @Override
  public Matrix get(TextureVideoView object) {
    return null;
  }
};
