void setup() {
  size(1000, 700);
  smooth();
  strokeWeight(2);
  clearScreen();
}
void draw() {
  if (mousePressed) {
    switch (mouseButton) {
      case LEFT:
      stroke(0);
      break;
      case RIGHT:
      stroke(255, 0, 0);
      break;
    }
    line(mouseX, mouseY, pmouseX, pmouseY);
  }
  if (keyPressed) {
    clearScreen();
  }
}

void clearScreen() {
  background(255);
}
