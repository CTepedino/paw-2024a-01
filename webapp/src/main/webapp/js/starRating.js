
function FixedStarRating(rating = 0){
    let i = 1;
    for(;i*2<=rating ;i++){
        document.write("<i class='material-icons small'>star</i>");
    }
    if ((i*2)-1 === rating){
        document.write("<i class='material-icons small'>star_half</i>");
        i++;
    }

    for(;i <= 5; i++){
        document.write("<i class='material-icons small'>star_border</i>");
    }

}