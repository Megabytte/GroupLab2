var circleX = 100
var circleY = 90
var radius = 70;

var canvas = document.getElementById('mycanvas');
var context = canvas.getContext('2d');

function draw()
{
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.beginPath();
    context.arc(circleX, circleY, radius, 0, 2 * Math.PI, false);
    context.fillStyle = 'green';
    context.fill();
    context.lineWidth = 5;
    context.strokeStyle = '#003300';
    context.stroke();
    context.closePath()
}

draw();
