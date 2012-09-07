void setup() {
  size(1000, 700);
  smooth();
  clearScreen();
  showInstruction();
}
void draw() {
  if (mousePressed) {
    switch (mouseButton) {
      case LEFT:
      stroke(0);
      strokeWeight(2);
      break;
      case RIGHT:
      stroke(255, 0, 0);
      strokeWeight(3);
      break;
    }
    line(mouseX, mouseY, pmouseX, pmouseY);
  }
  if (keyPressed) {
    if (key == 's' || key == 'S') {
      save("shot.png");
    } else if (key == ' ') {
      clearScreen();
    }
  }
}

void clearScreen() {
  background(255);
}

void showInstruction() {
  fill(180);
  String instr = "就是这样使用白板。左键画图，右键强调，空格键清除屏幕， S 键保存截图， Esc 键退出。\n\n小池制作 SoMe";
  text(instr, 100, 100);
}
