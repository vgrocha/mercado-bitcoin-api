(ns mercado-bitcoin.core-test
  (:require [clojure.test :refer :all]
            [mercado-bitcoin.btc :as btc]
            [mercado-bitcoin.btc :as ltc]))

(deftest btc-test
  (testing "request"
    (is (= (-> (btc/ticker) :ticker keys set)
           #{:date :high :sell :buy :low :last :vol}))
    (let [orderbook (-> (btc/orderbook))]
      (is (vector? (:asks orderbook)))
      (is (vector? (:bids orderbook))))))

(deftest ltc-test
  (testing "request"
    (is (= (-> (ltc/ticker) :ticker keys set)
           #{:date :high :sell :buy :low :last :vol}))
    (let [orderbook (-> (ltc/orderbook))]
      (is (vector? (:asks orderbook)))
      (is (vector? (:bids orderbook))))))

;;test the trader operations
