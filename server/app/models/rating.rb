class Rating < ActiveRecord::Base
  versioned
  belongs_to :app
end
