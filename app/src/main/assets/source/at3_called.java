
// Called Activity
getWindow().setEnterTransition(
        TransitionInflater.from(this)
            .inflateTransition(R.transition.slide_bottom));
getWindow().setReturnTransition(
        TransitionInflater.from(this)
            .inflateTransition(R.transition.explode));