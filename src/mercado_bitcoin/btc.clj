(ns mercado-bitcoin.btc
  (:require
   [mercado-bitcoin.core :refer :all]))

(def ticker-address "https://www.mercadobitcoin.com.br/api/ticker/")
(def orderbook-address "https://www.mercadobitcoin.com.br/api/orderbook/")
(def trades-address "https://www.mercadobitcoin.net/api/trades/")

;;all communications wires are set up
;;some syntactic sugar
(def trades #(info trades-address))

(def orderbook #(info orderbook-address))

(def ticker #(info ticker-address))

(defn orders-list-status [trader from-id to-id]
  (trader "OrderList" :from_id from-id :end_id to-id :pair "btc_brl"))

(defn order-status [trader id]
  (orders-list-status trader id id))

(defn create-order [trader type price volume]
  {:pre [(#{:buy :sell} type)]}
  (trader "Trade" :pair "btc_brl" :type (name type) :volume volume :price price))

(defn cancel-order [trader id]
  (trader "CancelOrder" :pair "btc_brl" :order_id id))
