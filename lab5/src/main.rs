extern crate ndarray;
extern crate ndarray_linalg;

use ndarray::prelude::*;
use ndarray_linalg::Solve;
use polynomials::{poly, Polynomial};

fn main() {
    // 8 + (s1' = s2') + (s2' = s3') + (s3' = s4') +
    // + (s1'' = s2'') + (s2'' = s3'') + (s3'' = s4'') + (14)
    // + (s1'' = 0) + (s4'' = 0) (16)

    let input_function = |x: &f64| (x * x + 2_f64) * (x - 1_f64).sqrt();
    let points = 16;
    let x: Vec<f64> = (0..points)
        .map(|x| 1_f64 + (x as f64) * 4_f64 / (points as f64))
        .collect();
    let y: Vec<f64> = x.iter().map(input_function).collect();

    println!("{0: <10} | {1: <10}", "x", "y");
    for i in 0..x.len() {
        println!("{0: <10} | {1: <10}", x[i], y[i]);
    }

    println!(
        "Lagrange:  = {}",
        polynomial_str(&make_lagrange_polynomial(&x, &y))
    );

    let spline_equations = make_spline_linear_equations(&x, &y);
    calculate_deviation(&spline_equations, &x, &input_function);
}

///makes lagrange polynomial on given points
fn make_lagrange_polynomial(
    input_values_x: &Vec<f64>,
    input_values_y: &Vec<f64>,
) -> Polynomial<f64> {
    let mut lagrange_polynomial: Polynomial<f64> = Polynomial::new();
    println!("LAGRANGE POLYNOMIAL\n");

    for i in 0..input_values_y.capacity() {
        let mut c_i = 1_f64;
        for j in 0..input_values_x.len() {
            if j != i {
                c_i *= input_values_x[i] - input_values_x[j];
            }
        }
        println!("c_{} = {:?}", i, c_i);

        let mut polynomial_i = poly![1_f64];
        for j in 0..input_values_x.len() {
            if j != i {
                polynomial_i *= poly![-input_values_x[j], 1_f64];
            }
        }
        println!("polynomial_{} = {}", i, polynomial_str(&polynomial_i));
        lagrange_polynomial += (poly![input_values_y[i]] * polynomial_i) / c_i;
    }
    lagrange_polynomial
}

// solves equations for spline polynomials coeff
// makes these polynomials for inserting in https://www.geogebra.org/calculator
fn make_spline_linear_equations(x: &Vec<f64>, y: &Vec<f64>) -> Vec<Polynomial<f64>> {
    let mut h_i: Vec<f64> = Vec::with_capacity(x.len() - 1);
    let c_i: Vec<f64> = Vec::with_capacity(x.len() - 1);
    let mut a_i: Vec<f64> = Vec::with_capacity(x.len() - 1);

    for i in 1..x.len() {
        h_i.push(x[i] - x[i - 1]);
        a_i.push(y[i - 1]);
    }

    let mut b = Vec::new();
    let mut a: Array2<f64> = Array2::<f64>::zeros((c_i.capacity(), c_i.capacity()));
    a[[0, 0]] = 1_f64;
    b.push(0_f64);

    for i in 2..y.len() - 1 {
        b.push(3_f64 * ((y[i] - y[i - 1]) / h_i[i - 1] - (y[i - 1] - y[i - 2]) / h_i[i - 1 - 1]));
        a[[i - 1, i - 2]] = h_i[i - 1 - 1];
        a[[i - 1, i - 1]] = 2_f64 * (h_i[i - 1 - 1] + h_i[i - 1]);
        a[[i - 1, i]] = h_i[i - 1];
    }
    a[[c_i.capacity() - 1, c_i.capacity() - 1]] = 1_f64;

    b.push(0_f64);
    let b: Array1<f64> = Array1::from_vec(b);

    let roots = a.solve_into(b).unwrap();
    let c_i = roots.into_raw_vec();

    let mut b_i: Vec<f64> = Vec::with_capacity(x.len() - 1);
    let mut d_i: Vec<f64> = Vec::with_capacity(x.len() - 1);
    for i in 1..x.len() {
        if i != x.len() - 1 {
            d_i.push((c_i[i] - c_i[i - 1]) / (3_f64 * h_i[i - 1]));
            b_i.push(
                (y[i] - y[i - 1]) / h_i[i - 1] - h_i[i - 1] * (c_i[i] + 2_f64 * c_i[i - 1]) / 3_f64,
            );
        } else {
            d_i.push(-c_i[c_i.len() - 1] / (3_f64 * h_i[c_i.len() - 1]));
            b_i.push(
                (y[c_i.len() - 1] - y[c_i.len() - 1 - 1]) / h_i[c_i.len() - 1]
                    - 2_f64 * h_i[c_i.len() - 1] * c_i[c_i.len() - 1] / 3_f64,
            );
        }
    }
    println!(
        "SPLINE POLYNOMIALS\na_i:{:?} \n b_i:{:?} \n c_i:{:?} \n d_i:{:?} \n",
        a_i, b_i, c_i, d_i
    );

    let mut spline_polynomials: Vec<Polynomial<f64>> = Vec::new();
    for i in 1..x.len() {
        spline_polynomials.push(poly![a_i[i - 1], b_i[i - 1], c_i[i - 1], d_i[i - 1]]);
        println!(
            "if[{}< x <{}, {}]",
            x[i - 1],
            x[i],
            spline_polynomial(&spline_polynomials[spline_polynomials.len() - 1], x[i - 1])
        );
    }

    spline_polynomials
}

// transforms polynomial vec into string
fn polynomial_str(polynomial_vec: &Polynomial<f64>) -> String {
    let mut str: String = String::new();
    for (i, value) in polynomial_vec.iter().enumerate() {
        str.push_str(&format!("{}x^{} + ", value, i));
    }
    str.remove(str.len() - 1);
    str.remove(str.len() - 1);
    str.remove(str.len() - 1);
    str
}

// makes spline polynomial on point x0 and polynomial vec
fn spline_polynomial(polynomial_vec: &Polynomial<f64>, x0: f64) -> String {
    let mut str: String = String::new();
    for (i, value) in polynomial_vec.iter().enumerate() {
        if i == 0 {
            str.push_str(&format!("{} + ", value));
        } else {
            str.push_str(&format!("{}(x - {})^{} + ", value, x0, i));
        }
    }
    str.remove(str.len() - 1);
    str.remove(str.len() - 1);
    str.remove(str.len() - 1);
    str
}

// calculates deviation for y_original/y_custom and displays table
fn calculate_deviation(
    spline_vec: &Vec<Polynomial<f64>>,
    x: &Vec<f64>,
    input_function: &dyn Fn(&f64) -> f64,
) {
    let get_y = |x0: &f64, x: &f64, poly: &Polynomial<f64>| -> f64 {
        let mut result: f64 = 0_f64;
        let mut temp: f64;
        for (i, value) in poly.iter().enumerate() {
            if i != 0 {
                temp = (x - x0).powf(i as f64);
            } else {
                temp = 1_f64;
            }
            result += temp * value;
        }
        result
    };

    let mut x_new = Vec::new();
    let mut y_custom: Vec<f64> = Vec::new();
    let points_num = 4_f64;
    for i in 1..x.len() {
        for j in 1..=(points_num as i32) {
            x_new.push(x[i - 1] + (j as f64) * (x[i] - x[i - 1]) / points_num);
            y_custom.push(get_y(
                &x[i - 1],
                &x_new[x_new.len() - 1],
                &spline_vec[i - 1],
            ))
        }
    }
    let y_original: Vec<f64> = x_new.iter().map(input_function).collect();
    let mut deviation_abs: Vec<f64> = Vec::new();
    for i in 0..y_original.len(){
        deviation_abs.push((y_original[i] - y_custom[i]).abs())
    }

    println!(
        "\nDEVIATION\n{0: <20} | {1: <20} | {2: <20} | {3: <20}",
        "x", "y_original", "y_custom", "|deviation|"
    );
    for i in 0..x_new.len() {
        println!(
            "{0: <20} | {1: <20} | {2: <20} | {3: <20}",
            x_new[i], y_original[i], y_custom[i], deviation_abs[i]
        );
    }
}
