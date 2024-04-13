const pegs = $(".peg");

pegs.mouseenter(function () {
    $(".peg:hover").css("animation-name", "in");
})

pegs.mouseleave(function () {
    $(".peg").css("animation-name", "out");
})

