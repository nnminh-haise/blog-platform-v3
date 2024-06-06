console.log('5')
$(document).ready(() => {
    const postsToShow = 5; // Number of posts to show each time
    const totalPosts = $('.post').length; // Total number of posts
    let postsShown = 0; // Number of posts currently shown
    console.log('post length', $('.post').length)
    const showMorePosts = () => {
    $('.post').slice(postsShown, postsShown + postsToShow).fadeIn();
    postsShown += postsToShow;

    // Hide the button if all posts are shown
    if (postsShown >= totalPosts) {
    $('#loadMore').fadeOut();
}
};

    // Initial load
    showMorePosts();

    // On button click, show more posts
    $('#loadMore').click(() => {
    showMorePosts();
});
});

