(ns mercado-bitcoin.core-test
  (:require [clojure.test :refer :all]
            [mercado-bitcoin.core :refer :all]))

(deftest a-test
  (testing "request"
    (is (= (-> (info-ticker) :ticker keys set)
           #{:date :high :sell :buy :low :last :vol}))
    (let [orderbook (-> (info-orderbook))]
      (is (vector? (:asks orderbook)))
      (is (vector? (:bids orderbook))))))

;;test the trader operations
