# Newton-Raphson iteration fractals

Generating fractal images based on Newton-Raphson iteration by approximating k-th order Taylor-polynomial.


> k-times differentiable functions can be approximated by k-th order Taylor-polynomial:

![eq1](https://latex.codecogs.com/gif.latex?f%20%28x_0&plus;%5Calpha%29%20%3D%20f%28x_0%29%20&plus;%20f%27%28x_0%29*%5Calpha%20&plus;%20%5Cfrac%7B1%7D%7B2%21%7Df%27%27%28x_0%29%5Calpha%5E2%20&plus;%5Cfrac%7B1%7D%7B3%21%7Df%27%27%27%28x_0%29%5Calpha%5E3%20&plus;%20...)

>Let x1 be a certain point somewehere close to x0:

![eq2](https://latex.codecogs.com/gif.latex?x_1%20%3D%20x_0%20&plus;%20%5Calpha)

>By substituting x1 and x0 we obtain:

![eq3](https://latex.codecogs.com/gif.latex?f%20%28x_1%29%20%3D%20f%28x_0%29%20&plus;%20f%27%28x_0%29*%28x_1-x_0%29%20&plus;%20%5Cfrac%7B1%7D%7B2%21%7Df%27%27%28x_0%29%28x_1-x_0%29%5E2%20&plus;%20...)

>Performing restrictions on linear approximation:

![eq4](https://latex.codecogs.com/gif.latex?f%28x_1%29%5Capprox%20f%28x_0%29%20&plus;%20f%27%28x_0%29%28x_1-x_0%29)

>We are looking for x1 for which the function is equal to zero:

![eq5](https://latex.codecogs.com/gif.latex?x_1%3Dx_0%20-%20%5Cfrac%7Bf%28x_0%29%7D%7Bf%27%28x_0%29%7D)

>Finally, we acquire an iterative expression that is known as Newton-Raphson iteration:

![eq6](https://latex.codecogs.com/gif.latex?x_%7Bn&plus;1%7D%3Dx_n%20-%20%5Cfrac%7Bf%28x_n%29%7D%7Bf%27%28x_n%29%7D)


#### How it works?
![Newton fractal generation](https://github.com/damjanvucina/newton-fractals/blob/master/newton.gif)



# Ray casting algorithm
Rendering 3D scenes using Ray casting algorithm. Parallelizing calculations by using ForkJoin and RecursiveAction.

![Rendered scene](https://github.com/damjanvucina/newton-fractals/blob/master/raycaster.png)


