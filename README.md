# mercado-bitcoin

A Clojure library designed to use the mercadobitcoin.com.br API for
trading Bitcoins.

The library abstract all the plumbing necessary to correctly interact
with the API. The returned value is the JSON parsed into a Clojure
map, where the dates are clj-time objects.

## Usage

There are basically two types of access to the API: 
1) obtaining information 
2) manipulating the trade orders

### Obtaining information
* Retrieve the current exchange information

```
mercado-bitcoin.core> (info-ticker)
;=> {:ticker {:high 735.58086,
              :low 690.01011,
              :vol 148.13091468,
              :last 706.00001,
              :buy 708.47146,
              :sell 717.29897,
              :date #<DateTime2015-04-29T03:00:41.000Z>}}
```

* Retrieve current orderbook
```
mercado-bitcoin.core> (info-orderbook)
;=> {:asks [[717.29897 0.08042057] [717.29966 1.63798] ... ],
     :bids [[708.14374 0.61287116] [708.09008 1.47339109] ...]}
```

### Manipulating trade orders
First you need to call a trader to be your operator, but passing your credentials

```
mercado-bitcoin.core> (def trader (get-trader cred/mercado-tapi-codigo cred/mercado-tapi-chave cred/PIN))
#'mercado-bitcoin.core/trader
```

Than you can send a buy order

```
mercado-bitcoin.core> (create-order trader :buy 1 0.01)
;=> {:return {:1945979 {:status "active",
                        :operations {},
                        :created #<DateTime 2015-04-29T20:47:38.000Z>,
                        :price 1.0,
                        :volume 0.01,
                        :pair "btc_brl",
                        :type "buy"}},
     :success 1}
```

Then retrieve current status from the order

```
mercado-bitcoin.core> (order-status trader 1945979)
;=> {:return {:1945979 {:status "active",
              :operations {},
              :created #<DateTime 2015-04-29T20:47:38.000Z>,
              :price 1.0,
              :volume 0.01,
              :pair "btc_brl",
              :type "buy"}},
     :success 1}
```

And if you changed your mind, cancel it

```
mercado-bitcoin.core> (cancel-order trader 1945979)
;=> {:return {:1945979 {:status "canceled",
                        :operations {},
                        :created #<DateTime 2015-04-29T20:47:38.000Z>,
                        :price 1.0,
                        :volume 0.01,
                        :pair "btc_brl",
                        :type "buy"}},
     :success 1}
```
     
How about a sell order?

```
mercado-bitcoin.core> (create-order trader :sell 10000 0.01)
;=> {:return {:1946021 {:status "active",
                        :operations {},
                        :created #<DateTime 2015-04-29T20:50:09.000Z>,
                        :price 10000.0,
                        :volume 0.01,
                        :pair "btc_brl",
                        :type "sell"}},
     :success 1}

mercado-bitcoin.core> (cancel-order trader 1946021)
;=> {:return {:1946021 {:status "canceled",
                        :operations {},
                        :created #<DateTime 2015-04-29T20:50:09.000Z>,
                        :price 10000.0,
                        :volume 0.01,
                        :pair "btc_brl",
                        :type "sell"}},
     :success 1}
```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
