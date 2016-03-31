(ns frac.regex.core
  (:require [quil [core :as q] [applet :as a] [middleware :as m]]))

(defn add-pts [[k [x y i]]]
  (let [i (/ i 2)]
    [[(str k "2") [x y i]]
     [(str k "1") [(+ x i) y i]]
     [(str k "3") [x (+ y i) i]]
     [(str k "4") [(+ x i) (+ y i) i]]]))

(defn pairs [i]
  (nth (iterate (partial mapcat add-pts) [["" [0 0 512]]]) i))

(defn setup [] (q/no-stroke) (q/no-loop) {:pattern #"5"})

(defn draw-state [{pattern :pattern}]
  (q/background 0 0 0)
  (q/fill 255)
  (doseq [[s [x y i]] (pairs 8)
          :when (re-find pattern s)]
    (q/fill (- 255 (* 255.0 (count (re-seq pattern s)) (/ 7.0))))
    (q/rect x y i i)))

 (q/defsketch regexfractal
   :title "Regex fractal viewer"
   :size [512 512]
   :setup setup, :draw draw-state
   :middleware [m/fun-mode])

(defn pattern! [pat]
  (binding [a/*applet* regexfractal]
    (swap! (q/state-atom) assoc :pattern pat)
    (q/redraw)))

(comment

 ;; mi lesz ez??
 (pattern! #"^[123]*$")

 (pattern! #"(?:12|23|34|41)")
 (pattern! #"(?:13|31|24|42)")

 (pattern! #"([12])([34]*\1)")

 (pattern! #"^[34]*2")
 ; abacaba
 (pattern! #"^[13]*[24]*$")
 ;; abacaba szamrendszer, lepcsok, hiperkocka - michael naylor

 ;;http://www.abacaba.org/abacaba-article.pdf



 )
