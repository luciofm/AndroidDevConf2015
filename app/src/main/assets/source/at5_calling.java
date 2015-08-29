
// Calling Activity
getWindow().setExitTransition(
        inflater.inflateTransition(R.transition.slide_top));

getWindow().setReenterTransition(
        inflater.inflateTransition(R.transition.slide_bottom));