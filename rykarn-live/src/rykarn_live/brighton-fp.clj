(ns rykarn-live.brighton-fp
  (:use [overtone.live]
        [rykarn-live.fader]))


;Our first synthesizer. Works, but it is hard to quickly see how it is built up. Also harder to change and expand upon.
(defsynth fm-synth [note 70]
  (let [base-freq (midicps note)]
    (out [0 1] (lpf (* (sin-osc base-freq (* 7 (sin-osc base-freq))) (env-gen (env-perc 0 1) :action FREE) 0.3) 600))))

;By using ->> we are able to code the sigal processing chain as how we envisioned the synthesizer!
(defsynth fm-synth2 [note 70 freq-mult1 2 freq-mult2 3 index1 2 index2 2 decay1 0.5 decay2 1 decay3 2 filter-freq 6000]
  (let [base-freq (midicps note)]
    (->> (sin-osc (* freq-mult1 base-freq))
         (* index1)
         (* (env-gen (env-perc 0 decay1)))
         (sin-osc (* freq-mult2 base-freq))
         (* index2)
         (* (env-gen (env-perc 0 decay2)))
         (sin-osc base-freq)
         (* (env-gen (env-perc 0 decay3) :action FREE))
         (#(lpf % filter-freq))
         (* 0.3)
         (out [0 1]))))

(defn trigger []
  (doseq [note (chord :C4 :minor)]
    (fm-synth2
            :note note
            :freq-mult1 2   ;setting these to fractional values creates harsh, dissonant sounds
            :freq-mult2 3
            :index1 2       ;these control how strong the FM-effect will be
            :index2 2
            :decay1 0.5     ;these control the decay time of each oscillator
            :decay2 1
            :decay3 2
            :filter-freq 5000)))

;the items in this vector determine what rhythms the sequencer can choose
(def choices [
              [1 1 1]      ;comment this item and uncomment some of the others!
              ;[1 1 4]     ;play 1 note of 1/4 length
              ;[2 1 8]
              ;[3 1 12]    ;play 3 notes of 1/12 length etc.
              ;[4 1 16]
              ;[6 1 24]
              ])

(defn play-seq [apply-times] (doseq [time apply-times] (apply-at time trigger)))

(defn rand-choice [] (get choices (rand-int (count choices))))

(defn times [start-time bpm choice]
  (let [beat-time (* (* (/ 60 bpm) 1000) 4) 
        delta-time (/ (* (get choice 1) beat-time) (get choice 2))
        num-events (get choice 0)
        time-of-event #(+ start-time (* delta-time %))]
    [(map time-of-event (range num-events)) (time-of-event num-events)]))

(defn sequence-gen [t]
  (let [[trigger-times next-time] (times t 150 (rand-choice))]
    (play-seq trigger-times)
    (apply-by next-time #'sequence-gen [next-time])))

(sequence-gen (now))
