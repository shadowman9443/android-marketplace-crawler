class Target < ActiveRecord::Base
  has_many :app_targets
  has_many :apps, :through => :app_targets
end
