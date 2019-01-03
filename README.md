# Tone Generator

Pure tone signal generator for android devices. 

## Getting Started

* You need to have Android SDK and Android Studio installed.
* Run the app using Android Studio or deploy the apk. to your device of choice.

The frequency can be changed by either adjusting the slider or by using the textbox (it only accepts
integers).

As of now, the only available waveform is a sine wave.

### Prerequisites

* Android Minimun SDK: 5.0 (Lollipop)

## Info on wave synthesis

Three types of waves are generated: sine waves, square waves, and sawtooth waves.

Sine waves are the most basic type. The equation that defines their behaviour
is as follows:

<a href="https://www.codecogs.com/eqnedit.php?latex=x(t)&space;=&space;A*sin(2&space;\pi&space;f&space;t&space;&plus;&space;\phi)" target="_blank"><img src="https://latex.codecogs.com/gif.latex?x(t)&space;=&space;A*sin(2&space;\pi&space;f&space;t&space;&plus;&space;\phi)" title="x(t) = A*sin(2 \pi f t + \phi)" /></a>

However, for practical purposes, it is more useful to define it as:

<a href="https://www.codecogs.com/eqnedit.php?latex=x(t)&space;=&space;A*sin(\phi&space;(t))" target="_blank"><img src="https://latex.codecogs.com/gif.latex?x(t)&space;=&space;A*sin(\phi&space;(t))" title="x(t) = A*sin(\phi (t))" /></a>

Where

<a href="https://www.codecogs.com/eqnedit.php?latex=\phi&space;(t_0)&space;=&space;0" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\phi&space;(t_0)&space;=&space;0" title="\phi (t_0) = 0" /></a>

And

<a href="https://www.codecogs.com/eqnedit.php?latex=\phi&space;(t_1)&space;=&space;\phi&space;(t_0)&space;&plus;&space;2&space;\pi&space;f&space;\Delta&space;t" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\phi&space;(t_1)&space;=&space;\phi&space;(t_0)&space;&plus;&space;2&space;\pi&space;f&space;\Delta&space;t" title="\phi (t_1) = \phi (t_0) + 2 \pi f \Delta t" /></a>

Where <a href="https://www.codecogs.com/eqnedit.php?latex=\Delta&space;t&space;\\" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\Delta&space;t&space;\\" title="\Delta t \\" /></a>
is the minimum step between samples.

This is known as a phase accumulator, since we are defining each sample of the sine wave
in relation to the previous sample, in a cumulative sum. The advantage of this approach
is that we can change the frequency dynamically and there will be no discontinuities in the generated
wave. This is the logic implemented for sine wave generation.

Using a Fourier expansion, a square wave can be represented as an infinite sum
of sinusoidal waves:

<a href="https://www.codecogs.com/eqnedit.php?latex=x_{square}(t)&space;=&space;A&space;*&space;\frac{4}{\pi}&space;\sum\limits_{n&space;=&space;0}^{\infty}&space;\frac{sin(2&space;\pi&space;(2&space;n&space;&plus;&space;1)&space;f&space;t)}{2&space;n&space;&plus;&space;1}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?x_{square}(t)&space;=&space;A&space;*&space;\frac{4}{\pi}&space;\sum\limits_{n&space;=&space;0}^{\infty}&space;\frac{sin(2&space;\pi&space;(2&space;n&space;&plus;&space;1)&space;f&space;t)}{2&space;n&space;&plus;&space;1}" title="x_{square} (t) = A * \frac{4}{\pi} \sum\limits_{n = 0}^{\infty} \frac{sin(2 \pi (2 n + 1) f t)}{2 n + 1}" /></a>

The generation of complex waves by adding their sinusoidal components is known as
additive synthesis. The app generates square waves using this method, with a limited number of
sinusoidal waves (20).

The same is true of sawtooth wave generation, the only difference is the generating equation:

<a href="https://www.codecogs.com/eqnedit.php?latex=x_{saw}(t)&space;=&space;[\frac{A}{2}&space;-&space;\frac{A}{\pi}]&space;\sum\limits_{n&space;=&space;0}^{\infty}&space;(-1)^n&space;\frac{sin(2&space;\pi&space;n&space;f&space;t)}{n}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?x_{saw}(t)&space;=&space;[\frac{A}{2}&space;-&space;\frac{A}{\pi}]&space;\sum\limits_{n&space;=&space;0}^{\infty}&space;(-1)^n&space;\frac{sin(2&space;\pi&space;n&space;f&space;t)}{n}" title="x_{saw}(t) = [\frac{A}{2} - \frac{A}{\pi}] \sum\limits_{n = 0}^{\infty} (-1)^n \frac{sin(2 \pi n f t)}{n}" /></a>


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
