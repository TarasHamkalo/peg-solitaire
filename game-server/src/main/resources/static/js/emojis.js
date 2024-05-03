let container = document.getElementById('animate')
const bottleHeight = container.clientHeight - 33;
let emoji = ['🌽', '🍇', '🍌', '🍒', '🍕', '🍷', '🍭', '💖', '💩', '🐷', '🐸', '🐳', '🎃', '🎾', '🌈', '🍦', '💁', '🔥', '😁', '😱', '🌴', '👏', '💃'];
let circles = [];

function emojiRain(count) {
    container.innerHTML = "";
    circles = [];
    for (let i = 0; i < count * 5; i++) {
        for (let j = 0; j < container.clientWidth - 10; j += container.clientWidth / 10) {
            addCircle(i * 150, [j, j], emoji[Math.floor(Math.random() * emoji.length)]);
        }
    }

}

function addCircle(delay, range, color) {
    setTimeout(function() {
        var c = new Circle(range[0] + Math.random() * range[1], 20 + Math.random() * 4, color, {
            x: -0.15 + Math.random() * 0.3,
            y: 1 + Math.random() * 3
        }, range);
        circles.push(c);
        container.appendChild(c.element);
    }, delay);
}

function Circle(x, y, c, v, range) {
    this.x = x;
    this.y = y;
    this.color = c;
    this.v = v;
    this.animated = true;
    this.range = range;
    this.element = document.createElement('span');
    this.element.style.display = 'block';
    this.element.style.opacity = 0;
    this.element.style.position = 'absolute';
    this.element.style.fontSize = '1.5rem';
    this.element.style.color = 'hsl('+(Math.random()*360|0)+',80%,50%)';
    this.element.innerHTML = c;
    this.element.style.opacity = 1;

    this.update = function() {
        if (!this.animated) {
            return;
        }

        for (let i = 0; i < circles.length; i++) {
            let other = circles[i];
            if (this === other || other.animated) {
                continue
            }

            let dx = other.x - this.x;
            let dy = other.y - this.y;
            let distance = Math.sqrt(dx * dx + dy * dy);

            let minDistance = this.element.clientHeight;
            if (distance < minDistance / 2.5) {
                this.element.style.transform = 'translate3d(' + this.x + 'px, ' + other.y + 'px, 0px)';
                this.animated = false;
                return;
            }

        }

        if (this.y + this.element.clientHeight >= bottleHeight) {
            let finalPos = container.clientHeight - this.element.clientHeight
            this.element.style.transform = 'translate3d(' + this.x + 'px, ' +  finalPos + 'px, 0px)';
            this.animated = false;
            return;
        }

        this.y += this.v.y;
        this.x += this.v.x;
        this.element.style.transform = 'translate3d(' + this.x + 'px, ' + this.y + 'px, 0px)';
    };
}

function animate() {
    for (let i = 0; i < circles.length; i++) {
        if (circles[i].animated) {
            circles[i].update();
        }
    }

    requestAnimationFrame(animate);
}

animate()