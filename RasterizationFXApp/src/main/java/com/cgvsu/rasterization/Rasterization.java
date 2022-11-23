package com.cgvsu.rasterization;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


public class Rasterization {

    private static int sign(int x) {
        return Integer.compare(x, 0);
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public static void drawLine(
            final GraphicsContext graphicsContext,
            int x0, int y0, int x1, int y1,
            final Color color1, Color color2) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;


        dx = x1 - x0;
        dy = y1 - y0;

        incx = sign(dx);
        /*
         * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
         * справа налево по иксу, то incx будет равен -1.
         * Это будет использоваться в цикле постороения.
         */
        incy = sign(dy);
        /*
         * Аналогично. Если рисуем отрезок снизу вверх -
         * это будет отрицательный сдвиг для y (иначе - положительный).
         */

        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy)
        //определяем наклон отрезка:
        {
            /*
             * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
             * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
             * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
             * по y сдвиг такой отсутствует.
             */
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        x = x0;
        y = y0;
        err = el / 2;


        int i = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0);
        int i1 = (x - x0) * (x - x0) + (y - y0) * (y - y0);
        double res = (Math.pow(i1, 0.5)) / (Math.pow(i, 0.5));

        double red = color1.getRed();
        double blue = color1.getBlue();
        double green = color1.getGreen();
        double red2 = color2.getRed();
        double blue2 = color2.getBlue();
        double green2 = color2.getGreen();
        double endR = (red + (red2 - red) * res) * 255;
        double endG = (green + (green2 - green) * res) * 255;
        double endB = (blue + (blue2 - blue) * res) * 255;
        int r2 = (int) Math.abs(endR);
        int g2 = (int) Math.abs(endG);
        int b2 = (int) Math.abs(endB);

        pixelWriter.setColor(x, y, Color.rgb(r2, g2, b2));


        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            } else {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }
            i = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0);
            i1 = (x - x0) * (x - x0) + (y - y0) * (y - y0);
            res = (Math.pow(i1, 0.5)) / (Math.pow(i, 0.5));

            red = color1.getRed();
            blue = color1.getBlue();
            green = color1.getGreen();
            red2 = color2.getRed();
            blue2 = color2.getBlue();
            green2 = color2.getGreen();
            endR = (red + (red2 - red) * res) * 255;
            endG = (green + (green2 - green) * res) * 255;
            endB = (blue + (blue2 - blue) * res) * 255;
            r2 = (int) Math.abs(endR);
            g2 = (int) Math.abs(endG);
            b2 = (int) Math.abs(endB);


            pixelWriter.setColor(x, y, Color.rgb(r2, g2, b2));
        }
    }
}