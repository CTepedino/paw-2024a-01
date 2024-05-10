function initializeRating(value) {
    const allStars = document.querySelectorAll(".star");

    document.getElementById('rating').value = value;
    allStars.forEach((star, j) => {
        if (value >= (j + 1) * 2) {
            star.innerHTML = 'star';
        } else {
            star.innerHTML = 'star_border';
        }
    })
}

function setRating(){
    const allStars = document.querySelectorAll(".star");

    allStars.forEach((star, i) => {
        star.onclick = function () {
            let currentStarLevel = (i + 1) * 2;
            document.getElementById('rating').value = currentStarLevel;
            allStars.forEach((star, j) => {
                if (currentStarLevel >= (j + 1) * 2) {
                    star.innerHTML = 'star';
                } else {
                    star.innerHTML = 'star_border';
                }
            })
        }
    });
}